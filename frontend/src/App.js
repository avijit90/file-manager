import React, { Component } from 'react';
import reactLogo from './logo.svg';
import springBootLogo from './spring-boot-logo.png';
import './App.css';

class App extends Component {

    constructor(props) {
        super(props);
        this.handleDownloadChange = this.handleDownloadChange.bind(this);
        this.handleUploadChange = this.handleUploadChange.bind(this);
        console.log("React App is up and running !");
        this.state.serverAdd = window.location.hostname;
        console.log('currentHostname = '+ this.state.serverAdd)
    }

    state = {
        file: '',
        error: '',
        msg: '',
        downloadPath: '',
        uploadPath: '',
        serverAdd: ''
    }

    getEncodedValue = (rawString) => {
        return new Buffer(rawString).toString('base64')
    }

    handleDownloadChange = (e) => {
        this.setState({
            downloadPath: e.target.value
        });
    }

    handleUploadChange = (e) => {
        this.setState({
            uploadPath: e.target.value
        });
    }

    handleServerPathChange = (e) => {
        this.setState({
            serverAdd: e.target.value
        });
    }

    uploadFile = (event) => {
        event.preventDefault();
        this.setState({error: '', msg: ''});

        if(!this.state.file) {
          this.setState({error: 'Please upload a file.'})
          return;
        }

        let data = new FormData();
        data.append('file', this.state.file);
        data.append('name', this.state.file.name);
        var encodedValue = this.getEncodedValue(this.state.uploadPath);

        fetch('/file/upload?path=' + encodedValue, {
          method: 'POST',
          body: data
        }).then(response => {
          this.setState({error: '', msg: 'Sucessfully uploaded file'});
          this.setState({uploadPath: ''});
        }).catch(err => {
          this.setState({error: err});
        });

    }

    download = () => {

        fetch('/address/server')
                  .then(response => response.text())
                  .then(msg => {
                      this.setState({msg: msg});
                  });


        var encodedValue = this.getEncodedValue(this.state.downloadPath);
        setTimeout(() => {
          const response = {
            file: 'http://' + this.state.serverAdd + ':8080/file/download?path=' + encodedValue,
          };
          window.location.href = response.file;
          this.setState({error: '', msg: 'File download successfully initiated..'});
          this.setState({uploadPath: ''});
        }, 100);
    }

    onFileChange = (event) => {
        this.setState({
            file: event.target.files[0]
        });
    }

    hello = () => {
      fetch('/hello/currentTime')
          .then(response => response.text())
          .then(msg => {
              this.setState({msg: msg});
          });
    };

      render() {
        return (
          <div className="App">
            <header className="App-header">
              <img src={reactLogo} className="App-logo" alt="reactLogo" />
              <img src={springBootLogo} className="App-logo" alt="springBootLogo" />
            </header>
            <div className="App-intro">
              <h4 style={{color: '#f53520'}}>{this.state.error}</h4>
              <h4 style={{color: '#90e81c'}}>{this.state.msg}</h4>
              <h3>Check connectivity with a simple Handshake</h3>
              <button className="App-button" onClick={this.hello}>Handshake</button>
              </div>
            <div className="App-intro">
              <h3>Upload File</h3>
               Upload Location:
               <input className="App-input-text" type="text" value={this.state.uploadPath} onChange={this.handleUploadChange} />
               <br/>
               <input className="App-input-text" onChange={this.onFileChange} type="file"></input>
               <button className="App-button" onClick={this.uploadFile}>Upload</button>
            </div>
            <div className="App-intro">
                <h3>Download File</h3>
                Download path:
                <input className="App-input-text" type="text" value={this.state.downloadPath} onChange={this.handleDownloadChange} />
                <button className="App-button" onClick={this.download}>Download</button>
            </div>
          </div>
        );
      }
}

export default App;